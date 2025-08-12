export type Page<T> = {
    content: Array<T>;
    last: boolean;
    totalElements: number;
    totalPage: number;
    first: boolean;
    numberOfElements: number;
    size: number;
    number: number;
    empty: boolean;
  }
  
  export const initPage: Page<any> = {
    content: [],
    last: true,
    totalElements: 0,
    totalPage: 0,
    first: true,
    numberOfElements: 0,
    size: 0,
    number: 0,
    empty: true
  }
  