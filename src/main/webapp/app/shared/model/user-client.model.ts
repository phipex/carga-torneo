export interface IUserClient {
  id?: number;
  idOperator?: number;
  idUserOperator?: string;
  idTorneo?: number;
  acumulado?: number;
  lastReport?: Date;
}

export class UserClient implements IUserClient {
  constructor(
    public id?: number,
    public idOperator?: number,
    public idUserOperator?: string,
    public idTorneo?: number,
    public acumulado?: number,
    public lastReport?: Date
  ) {}
}
