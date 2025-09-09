export const options = {
  vus: 10, // Number of Virtual Users
  maxDuration: '5s', // Total duration of the test
  iterations: 10, // Total number of iterations to run
};

export function setup() {
  console.log('🔧 setup');
  return { v: 1 };
}

export default function (data) {
  console.log('🚀', JSON.stringify(data));
}

export function teardown(data) {
  console.log('🧹', JSON.stringify(data));
  if (data.v != 1) {
    throw new Error('incorrect data: ' + JSON.stringify(data));
  }
}
