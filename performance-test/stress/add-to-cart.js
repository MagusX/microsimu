import http from 'k6/http';
import { check } from 'k6';

let MAX_USERS = 1000;


export const options = {
    stages: [
        { duration: '10m', target: 1000 },
        { duration: '30m', target: 1000 },
        { duration: '10m', target: 0 }
    ]
};

export function setup() {
    let users = [];
    for (let i = 1; i <= MAX_USERS; i++) {
        const customer = `customer ${i}`

        users[i] = { 
            addedToCart: false, 
            addToCartData: JSON.stringify({
                items: [
                    {
                        'customerId': customer,
                        'productId': '74757624-b7d6-4c9e-bc41-88d5594cda31',
                        'name': 'item 1',
                        'price': 10,
                        'quantity': 5
                    },
                    {
                        'customerId': customer,
                        'productId': '25cbedfa-108e-45b6-b2d3-c6c3a1306b39',
                        'name': 'item 2',
                        'price': 15,
                        'quantity': 6
                    }
                ]
            }),
            getCartData: JSON.stringify({
                'customerId': customer
            })
        };
    }
    return users;
}

export default function(users) {
    let userId = __VU;
    let headers = { 'Content-Type': 'application/json' };

    if (!users[userId].addedToCart) {
        let res = http.post('http://localhost:4010/api/cart-order/add-to-cart', users[userId].addToCartData, { headers: headers }
        );

        check(res, { 'status was 200': (r) => {
            if (r.status == 200) {
                users[userId].addedToCart = true;
            }
            return r.status == 200;
        }});
    } else {
        http.post('http://localhost:4010/api/cart-order/get-cart', users[userId].getCartData, { headers: headers });
    }
};