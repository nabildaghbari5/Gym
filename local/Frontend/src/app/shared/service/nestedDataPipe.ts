import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'nestedData' })
export class NestedDataPipe implements PipeTransform {
  transform(items: any[], key: string): string {
    if (!items || !key) return '';
    return items.map(item => item[key]).join(', ');
  }
}