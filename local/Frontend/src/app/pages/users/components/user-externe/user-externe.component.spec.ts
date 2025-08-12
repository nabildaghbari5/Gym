import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserExterneComponent } from './user-externe.component';

describe('UserExterneComponent', () => {
  let component: UserExterneComponent;
  let fixture: ComponentFixture<UserExterneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserExterneComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserExterneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
