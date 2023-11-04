import http from 'k6/http';
import { check } from 'k6';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';
import { SharedArray } from 'k6/data';

let MAX_USERS = 1000;

const csvData = new SharedArray('another data name', function () {
    let d = papaparse.parse(open('./csv/cart_item.csv'), { header: true }).data;
    return d;
});

export const options = {
    stages: [
        { duration: '10m', target: 1000 },
        { duration: '30m', target: 1000 },
        { duration: '10m', target: 0 }
    ]
};

export function setup() {
    let dataList = {};

    for (const row of csvData) {
        if (!row || row == '') continue;

        const customer = row.customer_id
        if (!dataList[customer]) {
            dataList[customer] = { createdOrder: false, payload: {
                customerId: row.customer_id,
                distance: Math.floor(Math.random() * (100 - 20 + 1)) + 20,
                cartItemIds: [row.id]
            } };
        } else {
            dataList[customer]['payload']['cartItemIds'] = [...dataList[customer]['payload']['cartItemIds'], row.id];
        }
    }

    return dataList;
}

export default function(data) {
    let userId = __VU;
    let headers = { 'Content-Type': 'application/json' };
    const customer = `customer ${userId}`

    if (!data[customer].createdOrder) {
        let res = http.post('http://localhost:4010/api/cart-order/create-order', 
            JSON.stringify(data[customer].payload), { headers: headers }
        );

        check(res, { 'status was 200': (r) => {
            if (r.status == 200) {
                data[customer].createdOrder = true;
            }
            return r.status == 200;
        }});
    } else {
        http.post('http://localhost:4010/api/cart-order/get-cart', JSON.stringify({
            'customerId': customer
        }), { headers: headers });
    }
};