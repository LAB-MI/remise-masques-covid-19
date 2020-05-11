window.crypto = {
  subtle: {
    digest: jest.fn(),
  },
};

window.TextEncoder = class TextEncoder {
  constructor() {
    this.encode = (text) => text;
  }
};
