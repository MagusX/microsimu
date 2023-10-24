import http from 'k6/http';
import { check, sleep } from 'k6';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';
import { SharedArray } from 'k6/data';

let MAX_USERS = 3;

const csvData = new SharedArray('another data name', function () {
    let d = papaparse.parse(open('./csv/cart_order.csv'), { header: true }).data;
    return d;
});

export const options = {
    vus: MAX_USERS, // Key for Smoke test. Keep it at 2, 3, max 5 VUs
    duration: '1m' // This can be shorter or just a few iterations
    // iterations: 6
};

export function setup() {
    let dataList = {};

    for (const row of csvData) {
        if (!row || row == '') continue;

        const customer = row.customer_id
        if (!dataList[customer]) {
            dataList[customer] = { verifiedPayment: false, payload: {
                orderId: row.id,
                distance: row.distance,
                value: row.total_price,
                side: 'OUT',
                products: [
                    {
                        id: '74757624-b7d6-4c9e-bc41-88d5594cda31',
                        quantity: 5
                    }, {
                        id: '25cbedfa-108e-45b6-b2d3-c6c3a1306b39',
                        quantity: 6
                    }
                ]
            } };
        }
    }

    return dataList;
}

export default function(data) {
    let userId = __VU;
    let headers = { 'Content-Type': 'application/json' };
    const customer = `customer ${userId}`

    if (!data[customer].verifiedPayment) {
        let res = http.post('http://localhost:4010/api/payment/create', 
            JSON.stringify(data[customer].payload), { headers: headers }
        );

        check(res, { 'status was 200': (r) => {
            if (r.status == 200) {
                data[customer].verifiedPayment = true;
            }
            return r.status == 200;
        }});
    } else {
        http.post('http://localhost:4010/api/cart-order/get-cart', JSON.stringify({
            'customerId': customer
        }), { headers: headers });
    }
};