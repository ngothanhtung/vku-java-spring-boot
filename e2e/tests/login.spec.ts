import { test, expect } from '@playwright/test';

test('Login thành công', async ({ page }) => {
  await page.goto('https://aptech-tester.web.app/login');

  const h2 = page.locator('#root > div > div > div > main > div > h2');
  const textH2 = await h2.textContent();

  await page.locator('#login-form_username').fill('admin');
  await page.locator('#login-form_password').fill('Tester@123');

  await page.locator('#login-form > div:nth-child(4) > div > div > div > div > button').click();

  // Wait for 1000ms
  await page.waitForTimeout(500);
  await expect(page).toHaveURL('https://aptech-tester.web.app/home');
});

// Testcase 2: Đăng nhập không thành công với tài khoản sai thông tin
test('TC2 - Login: Không thành công', async ({ page }) => {
  await page.goto('https://aptech-tester.web.app/login');

  // Điền tên đăng nhập
  await page.locator('#login-form_username').fill('wrong-username');

  // Điền mật khẩu
  await page.locator('#login-form_password').fill('Tester@123');

  // Click vào nút Đăng nhập
  await page.locator('button:has-text("Đăng nhập")').click();

  // Wait for 1000ms
  await page.waitForTimeout(500);
  // Kiểm tra URL sau khi đăng nhập không thành công
  await expect(page).toHaveURL('https://aptech-tester.web.app/login');
});

// Testcase 3: Đăng nhập không thành công với tài khoản sai thông tin
test('TC3 - Login: Không thành công', async ({ page }) => {
  await page.goto('https://aptech-tester.web.app/login');

  // Điền tên đăng nhập
  await page.locator('#login-form_username').fill('admin');

  // Điền mật khẩu
  await page.locator('#login-form_password').fill('Tester@129');

  // Click vào nút Đăng nhập
  await page.locator('button:has-text("Đăng nhập")').click();

  // Wait for 1000ms
  await page.waitForTimeout(500);
  // Kiểm tra URL sau khi đăng nhập không thành công
  await expect(page).toHaveURL('https://aptech-tester.web.app/login');
});

// Testcase 4: Đăng nhập không thành công với tài khoản sai thông tin
test('TC4 - Login: Không thành công', async ({ page }) => {
  await page.goto('https://aptech-tester.web.app/login');

  // Điền tên đăng nhập
  await page.locator('#login-form_username').fill('admix');

  // Điền mật khẩu
  await page.locator('#login-form_password').fill('Tester@129');

  // Click vào nút Đăng nhập
  await page.locator('button:has-text("Đăng nhập")').click();

  // Wait for 1000ms
  await page.waitForTimeout(500);
  // Kiểm tra URL sau khi đăng nhập không thành công
  await expect(page).toHaveURL('https://aptech-tester.web.app/login');
});
