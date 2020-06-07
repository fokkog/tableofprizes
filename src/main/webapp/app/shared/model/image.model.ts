import { IUser } from 'app/core/user/user.model';

export interface IImage {
  id?: number;
  name?: string;
  url?: string;
  user?: IUser;
}

export class Image implements IImage {
  constructor(public id?: number, public name?: string, public url?: string, public user?: IUser) {}
}
