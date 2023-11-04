import http from 'k6/http';
import { check, sleep } from 'k6';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';
import { SharedArray } from 'k6/data';

const csvData = new SharedArray('another data name', function () {
    let d = papaparse.parse(open('./csv/cart_order.csv'), { header: true }).data;
    return d;
});

export const options = {
    stages: [
        { duration: '15m', target: 50000 },
        { duration: '30m', target: 50000 },
        { duration: '15m', target: 0 }
    ]
};

export function setup() {
    let dataList = {};

    for (const row of csvData) {
        if (!row || row == '') continue;

        const customer = row.customer_id
        if (!dataList[customer]) {
            dataList[customer] = {
                orderId: row.id,
                remainingKm: row.distance,
                distance: row.distance,
                status: 'DELIVERY_IN_PROGRESS'
            } 
        }
    }

    return dataList;
}

export default function(data) {
    let userId = __VU;
    let headers = { 'Content-Type': 'application/json' };
    const customer = `customer ${userId}`

    if (data[customer].remainingKm > 0) {
        const curKm = Math.floor(Math.random() * (3 - 1 + 1)) + 1

        let res = http.post('http://localhost:4000/api/delivery/track', 
            JSON.stringify({
                orderId: data[customer].orderId,
                km: curKm,
                distance: data[customer].distance,
                status: data[customer].status
            }), { headers: headers }
        );

        check(res, { 'status was 200': (r) => {
            if (r.status == 200) {
                data[customer].remainingKm -= curKm;
            }
            return r.status == 200;
        }});
        
        sleep(2);
    } else {
        http.post('http://localhost:4010/api/cart-order/get-cart', JSON.stringify({
            'customerId': customer
        }), { headers: headers });
    }
};