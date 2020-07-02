import { IPrize } from './prize.model';

export interface ITableOfPrizes {
  id?: number;
  name?: string;
  userId?: string;
  prizes?: IPrize[];
}

export class TableOfPrizes implements ITableOfPrizes {
  constructor(public id?: number, public name?: string, public userId?: string, public prizes?: IPrize[]) {}
}
