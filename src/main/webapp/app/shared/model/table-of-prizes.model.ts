export interface ITableOfPrizes {
  id?: number;
  name?: string;
  userId?: string;
}

export class TableOfPrizes implements ITableOfPrizes {
  constructor(public id?: number, public name?: string, public userId?: string) {}
}
