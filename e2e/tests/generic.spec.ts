import { test, expect } from '@playwright/test';

test('any', async ({ page }) => {
  const value = {
    name: 'Peter',
    age: 42,
    subjects: ['Math', 'Physics'],
    address: {
      city: 'Berlin',
      street: 'Main Street',
    },
  };

  expect(value).toEqual({
    name: expect.any(String),
    age: expect.any(Number),
    subjects: expect.any(Array),
    address: {
      city: expect.any(String),
      street: expect.any(String),
    },
  });
});

test('anything', async ({ page }) => {
  const value = { name: 'Peter', age: { number: 43 } };

  expect(value).toEqual({ name: expect.anything(), age: expect.anything() });
  // expect(value).toEqual({ age: expect.anything() });
});

test('arrayContaining', async ({ page }) => {
  expect(['WAITING', 'COMPLETED', 'CANCELED']).toEqual(expect.arrayContaining(['COMPLETED']));
  expect([1, 2, 3]).toEqual(expect.arrayContaining([3, 1]));
  expect([1, 2, 3]).toEqual(expect.arrayContaining([1, 2]));
});

test('closeTo', async ({ page }) => {
  expect({ prop: 0.1 + 0.2 }).not.toEqual({ prop: 0.3 });
  expect({ prop: 0.1 + 0.2 }).toEqual({ prop: expect.closeTo(0.3, 5) });
});

test('objectContaining', async ({ page }) => {
  // Assert some of the properties.
  expect({ name: 'Peter', age: 22 }).toEqual(expect.objectContaining({ name: 'Peter' }));

  // Matchers can be used on the properties as well.
  expect({ foo: 1, bar: 2 }).toEqual(expect.objectContaining({ bar: expect.any(Number) }));

  // Complex matching of sub-properties.
  expect({
    list: [1, 2, 3],
    obj: { prop: 'Hello world!', another: 'some other value' },
    extra: 'extra',
  }).toEqual(
    expect.objectContaining({
      list: expect.arrayContaining([2, 3]),
      obj: expect.objectContaining({ prop: expect.stringContaining('Hello') }),
    }),
  );
});

test('stringContaining', async ({ page }) => {
  expect('Hello world!').toEqual(expect.stringContaining('Hello'));
  expect('Hello world!').not.toEqual(expect.stringContaining('Goodbye'));
  expect('Hello world!').toEqual(expect.stringContaining('world'));
  expect('Hello world!').toEqual(expect.stringContaining('!'));
});

test('stringMatching', async ({ page }) => {
  // Regular Expression
  expect('0906123456').toEqual(expect.stringMatching(/\d{10}$/));
  expect('0916123456').toEqual(expect.stringMatching(/^(090|091|098)\d{7}$/));

  expect('123ms').toEqual(expect.stringMatching(/\d+m?s/));

  // Inside another matcher.
  expect({
    status: 'passed',
    time: '123ms',
  }).toEqual({
    status: expect.stringMatching(/passed|failed/),
    time: expect.stringMatching(/\d+m?s/),
  });
});

test('toBe', async ({ page }) => {
  const value = 1;
  expect(value).toBe(1);
});

test('toBeDefined', async ({ page }) => {
  const value = null;
  expect(value).toBeDefined();
});

test('toBeFalsy', async ({ page }) => {
  // Falsy bao gồm các giá trị: false, 0, "", null, undefined, NaN
  const value = 0;
  expect(value).toBeFalsy();
});

test('toBeNaN', async ({ page }) => {
  const value = NaN;
  expect(value).toBeNaN();
});

test('toContainEqual', async ({ page }) => {
  const value = [{ example: 1 }, { another: 2 }, { more: 3 }];
  expect(value).toContainEqual({ another: 2 });
  expect(new Set(value)).toContainEqual({ another: 2 });
});

test('toHaveProperty', async ({ page }) => {
  const value = {
    a: {
      b: [42],
    },
    c: true,
  };
  expect(value).toHaveProperty('a.b');
  expect(value).toHaveProperty('a.b', [42]);
  expect(value).toHaveProperty('a.b[0]', 42);
  expect(value).toHaveProperty('c');
  expect(value).toHaveProperty('c', true);
});

test('toMatchObject', async ({ page }) => {
  const value = {
    a: 1,
    b: 2,
    c: true,
  };
  expect(value).toMatchObject({ a: 1, c: true });
  expect(value).toMatchObject({ b: 2, c: true });

  expect([{ a: 1, b: 2 }]).toMatchObject([{ a: 1 }]);
});
