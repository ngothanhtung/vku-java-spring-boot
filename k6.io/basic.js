import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 100, // Number of Virtual Users
  maxDuration: '5s', // Total duration of the test
  iterations: 100, // Total number of iterations to run
};

// The default exported function is gonna be picked up by k6 as the entry point for the test script. It will be executed repeatedly in "iterations" for the whole duration of the test.
export default function () {
  // Make a GET request to the target URL
  http.get('https://server.aptech.io');

  // Sleep for 1 second to simulate real-world usage
  // sleep(1);
}
