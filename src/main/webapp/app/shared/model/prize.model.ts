import { IImage } from 'app/shared/model/image.model';

export interface IPrize {
  id?: number;
  quantity?: number;
  image?: IImage;
}

export class Prize implements IPrize {
  constructor(public id?: number, public quantity?: number, public image?: IImage) {}
}
