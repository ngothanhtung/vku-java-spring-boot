import { test, expect } from '@playwright/test';

import fs from 'fs';
import path from 'path';
import { parse } from 'csv-parse/sync';

// Đọc file csv
const records = parse(fs.readFileSync(path.join(__dirname, 'categories.csv')), {
  columns: true, // Tự động đọc header
  delimiter: ',', // Dấu phân cách
  skip_empty_lines: true, // Bỏ qua dòng trống
});

// Trước khi chạy mỗi test case
test.beforeEach(async ({ page }) => {
  await page.goto('https://os-admin.aptech.io/login');

  // Điền thông tin đăng nhập bằng locator
  await page.locator('#root > div.account-pages.my-5.pt-sm-5 > div > div > div > div.overflow-hidden.card > div.pt-0.card-body > div.p-2 > form > div:nth-child(1) > input').fill('tungnt@softech.vn'); // Thay X1 bằng selector thực tế
  await page.locator('#root > div.account-pages.my-5.pt-sm-5 > div > div > div > div.overflow-hidden.card > div.pt-0.card-body > div.p-2 > form > div:nth-child(2) > input').fill('123456789'); // Thay X2 bằng selector thực tế

  // Click vào nút đăng nhập
  await page.locator('#root > div.account-pages.my-5.pt-sm-5 > div > div > div > div.overflow-hidden.card > div.pt-0.card-body > div.p-2 > form > div.mt-3.d-grid > button').click(); // Thay X3 bằng selector thực tế

  // Chờ điều hướng và kiểm tra URL
  await page.waitForURL('https://os-admin.aptech.io/dashboard');

  await page.goto('https://os-admin.aptech.io/online-shop/data-management/categories');
});

// Lặp qua từng dòng khi đọc được
let index = 0;
for (const record of records) {
  index++;

  const testcaseName = 'Create a category OK ' + index + ': ' + record.name;

  test(testcaseName, async ({ page }) => {
    // Lưu trữ data cũ để xác thực
    const old_id = await page.locator('#layout-wrapper > div.main-content > div > div > div > div:nth-child(2) > div > div.ant-table-wrapper.css-d6xwx0 > div > div > div > div > div > table > tbody > tr:nth-child(1) > td:nth-child(1)').textContent();

    // Test data:
    const name = record.name;
    const description = record.description;

    // Test steps:
    await page.locator('#create-form_name').fill(name);
    await page.locator('#create-form_description').fill(description);

    // Click nút Save
    await page.locator('#create-form > div:nth-child(3) > div > div > div > div > button').click();

    // Chờ tầm 1s
    await page.waitForTimeout(1000);

    // Test expect:
    // Kiểm tra và xác nhận không có thông báo lỗi nào xuất hiện
    const error = page.locator('#layout-wrapper > div.main-content > div > div > div > div:nth-child(2) > div > div.card > div > div.ant-alert.ant-alert-error.ant-alert-with-description.css-d6xwx0');
    await expect(error).not.toBeVisible(); // Kiểm tra không có thông báo lỗi nào xuất hiện

    // Kiểm dữ liệu đã được tạo thành công
    const new_id = await page.locator('#layout-wrapper > div.main-content > div > div > div > div:nth-child(2) > div > div.ant-table-wrapper.css-d6xwx0 > div > div > div > div > div > table > tbody > tr:nth-child(1) > td:nth-child(1)').textContent();

    const new_name = await page
      .locator('#layout-wrapper > div.main-content > div > div > div > div:nth-child(2) > div > div.ant-table-wrapper.css-d6xwx0 > div > div > div > div > div > table > tbody > tr:nth-child(1) > td:nth-child(2) > span')
      .textContent();
    const new_description = await page
      .locator('#layout-wrapper > div.main-content > div > div > div > div:nth-child(2) > div > div.ant-table-wrapper.css-d6xwx0 > div > div > div > div > div > table > tbody > tr:nth-child(1) > td:nth-child(3) > span')
      .textContent();

    expect(new_id).not.toEqual(old_id); // Kiểm tra id mới khác id cũ
    expect(new_name).toEqual(name); // Kiểm tra name mới đúng với name đã nhập
    expect(new_description).toEqual(description); // Kiểm tra description mới đúng với description đã nhập
  });
}
