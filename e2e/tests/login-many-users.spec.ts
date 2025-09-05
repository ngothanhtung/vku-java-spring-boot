import { test, expect } from '@playwright/test';
import users from './data/users.json';
const URL = 'https://aptech-tester.web.app/login';

users.forEach((user) => {
  test(`Đăng nhập thành công - ${user.username}`, async ({ page }) => {
    await page.goto(URL);
    await page.locator('#login-form_username').fill(user.username);
    await page.locator('#login-form_password').fill(user.password);
    await page.locator('#login-form > div:nth-child(4) > div > div > div > div > button').click();
    // Dừng 500ms
    await page.waitForTimeout(500);
    // Mong đợi đăng nhập nhập sẽ chuyển hướng sang: https://aptech-tester.web.app/home
    await expect(page).toHaveURL('https://aptech-tester.web.app/home');
  });
});
