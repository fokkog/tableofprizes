import { IUser } from 'app/core/user/user.model';

export interface ITableOfPrizes {
  id?: number;
  name?: string;
  user?: IUser;
}

export class TableOfPrizes implements ITableOfPrizes {
  constructor(public id?: number, public name?: string, public user?: IUser) {}
}
