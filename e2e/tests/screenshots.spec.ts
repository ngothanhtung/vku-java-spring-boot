import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
  await page.goto('https://aptech-tester.web.app/login');
});

test('Screenshot - Capture', async ({ page }) => {
  await page.locator('#login-form_username').fill('admin');
  await page.locator('#login-form_password').fill('');
  await page.locator('button:has-text("Đăng nhập")').click();
  await page.waitForTimeout(500);

  // Chụp màn hình để lưu giữ lại
  await page.screenshot({ path: './tests/screen-shots/TC-LOGIN-REQUIRED-PASSWORD.png' });
});

test('TC1: Screenshot - Compare', async ({ page }) => {
  await page.locator('#login-form_username').fill('admin');
  await page.locator('#login-form_password').fill('');
  await page.locator('button:has-text("Đăng nhập")').click();
  await page.waitForTimeout(500);

  // Kiểm tra theo ảnh chụp
  // So sánh ảnh chụp với ảnh đã lưu trước đó
  const screenshot = await page.screenshot();

  await page.waitForTimeout(500);

  // Kiểm tra ảnh chụp có khớp với ảnh đã lưu trước đó không?
  expect(screenshot).toMatchSnapshot({
    name: './tests/screen-shots/TC-LOGIN-REQUIRED-PASSWORD.png',
  });
});
