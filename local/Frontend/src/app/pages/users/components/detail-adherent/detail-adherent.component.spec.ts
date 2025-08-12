import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailAdherentComponent } from './detail-adherent.component';

describe('DetailAdherentComponent', () => {
  let component: DetailAdherentComponent;
  let fixture: ComponentFixture<DetailAdherentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailAdherentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailAdherentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
