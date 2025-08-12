import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbonnementExpiresComponent } from './abonnement-expires.component';

describe('AbonnementExpiresComponent', () => {
  let component: AbonnementExpiresComponent;
  let fixture: ComponentFixture<AbonnementExpiresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbonnementExpiresComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AbonnementExpiresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
