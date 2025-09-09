import http from 'k6/http';
// Load data from customers.json file
import { SharedArray } from 'k6/data';
const customers = new SharedArray('customers', function () {
  return JSON.parse(open('./customers.json'));
});

console.log('Loaded ' + customers.length + ' customers');

export const options = {
  vus: customers.length, // Number of Virtual Users
  duration: '2s', // Total duration of the test
  iterations: customers.length, // Total number of iterations to run
};
// Hàm setup chạy một lần trước khi VU bắt đầu thực thi
export function setup() {
  // login to get access token
  const url = 'https://server.aptech.io/auth/login';
  const payload = JSON.stringify({
    username: 'tungnt@softech.vn',
    password: '123456789',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(url, payload, params);
  const token = res.json().access_token;
  return { token };
}
// Đầu ra của hàm setup sẽ được truyền vào hàm default: data
export default function (data) {
  // console.log('Using Access Token: ' + data.token);
  // // Call post api: https://server.aptech.io/online-shop/customers
  // const url = 'https://server.aptech.io/online-shop/customers';
  // const payload = JSON.stringify({
  //   firstName: 'Alisha',
  //   lastName: 'Haley',
  //   phoneNumber: '937-833-3111',
  //   address: '15774 Jimmy Flat',
  //   birthday: '1990-10-02',
  //   email: 'lauriane1.Glover@hotmail.com',
  // });
  // const params = {
  //   headers: {
  //     'Content-Type': 'application/json',
  //     Authorization: 'Bearer ' + data.token,
  //   },
  // };
  // http.post(url, payload, params);
}
