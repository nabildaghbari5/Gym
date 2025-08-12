import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserInterneComponent } from './user-interne.component';

describe('UserInterneComponent', () => {
  let component: UserInterneComponent;
  let fixture: ComponentFixture<UserInterneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserInterneComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserInterneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
