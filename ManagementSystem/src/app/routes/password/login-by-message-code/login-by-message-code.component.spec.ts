import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginByMessageCodeComponent } from './login-by-message-code.component';

describe('LoginByMessageCodeComponent', () => {
  let component: LoginByMessageCodeComponent;
  let fixture: ComponentFixture<LoginByMessageCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginByMessageCodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginByMessageCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
