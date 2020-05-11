// For a detailed explanation regarding each configuration property, visit:
// https://jestjs.io/docs/en/configuration.html

module.exports = {
  globals: { window: true },
  moduleFileExtensions: ["js", "json"],
  moduleNameMapper: {
    "^~/(.*)$": "<rootDir>/$1",
    "^@/(.*)$": "<rootDir>/$1",
  },
  setupFiles: ["<rootDir>/front/test/jest-setup.js"],
  testEnvironment: "jsdom",
  testMatch: ["<rootDir>/front/test/**/*.spec.js"],
  transform: {
    "^.+\\.js$": "babel-jest",
  },
};
