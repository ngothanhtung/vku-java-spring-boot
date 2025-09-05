import { test, expect } from '@playwright/test';

// Trước khi chạy mỗi test case
test.beforeEach(async ({ page }) => {
  await page.goto('http://127.0.0.1:5500/index.html');
  // await page.goto('https://aptech-tester.web.app/ticket-booking');
});

test('toBeChecked', async ({ page }) => {
  const locator = page.locator('#remember');
  await expect(locator).toBeChecked();
});

test('toBeDisabled', async ({ page }) => {
  const locator = page.locator('body > div.container.mt-5 > form > button');
  await expect(locator).toBeDisabled();
});

test('toBeEditable', async ({ page }) => {
  const locator = page.locator('#username');
  await expect(locator).toBeEditable();
});

test('toBeEmpty', async ({ page }) => {
  const locator = page.locator('#empty');
  await expect(locator).toBeEmpty();
});

test('toBeFocused', async ({ page }) => {
  const locator = page.locator('#username');
  await expect(locator).toBeFocused();
});

// toBeInViewport
test('toBeInViewport', async ({ page }) => {
  const locator = page.locator('#button1');
  // scroll to bottom of page
  await page.evaluate(() => {
    window.scrollTo(0, document.body.scrollHeight);
  });
  await expect(locator).toBeInViewport();
});

// toContainText
test('toContainText', async ({ page }) => {
  const locator = page.locator('#address');
  await expect(locator).toContainText('24 Lê Thánh Tôn');
});

test('booking', async ({ page }) => {
  await page.locator('#ticket-booking-form_fullname').fill('Nguyen Van An');
  await page.locator('#ticket-booking-form_address').fill('24 Lê Thánh Tôn');
  await page.locator('#ticket-booking-form_idnumber').fill('201455443');
  await page.locator('#ticket-booking-form_year').fill('1990');
  // Send key ENTER
  await page.keyboard.press('Tab');

  await page.getByText('ECONOMY (Phổ thông)').click();

  await page.locator('#ticket-booking-form > div:nth-child(6) > div > div > div > div > button').click();
  const locator = page.locator('#root > div > div > div > main > div > div.ant-row.css-1yacf91 > div:nth-child(2) > div > div.ant-result-title');
  await expect(locator).toContainText('Tiền vé: 3.000.000 VNĐ');
});

test('toHaveAttribute', async ({ page }) => {
  const locator = page.locator('#link');
  await expect(locator).toHaveAttribute('target', '_blank');
  await expect(locator).toHaveAttribute('title');
  await expect(locator).toHaveAttribute('title', 'Website của Aptech Đà Nẵng');
});

// toHaveCount
test('toHaveCount', async ({ page }) => {
  const locator = page.locator('input');
  await expect(locator).toHaveCount(3);
});

// toHaveCSS
test('toHaveCSS', async ({ page }) => {
  const locator = page.locator('#button1');
  await expect(locator).toHaveCSS('background-color', 'rgb(255, 255, 0)');
  await expect(locator).toHaveCSS('color', 'rgb(0, 0, 0)');
  await expect(locator).toHaveCSS('height', '60px');
  await expect(locator).toHaveCSS('border-radius', '12px');
});

test('toHaveValue', async ({ page }) => {
  const locator = page.locator('#username');
  await expect(locator).toHaveValue('Nguyen Van A');
});
