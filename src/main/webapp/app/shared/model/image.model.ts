export interface IImage {
  id?: number;
  name?: string;
  url?: string;
  userId?: string;
}

export class Image implements IImage {
  constructor(public id?: number, public name?: string, public url?: string, public userId?: string) {}
}
