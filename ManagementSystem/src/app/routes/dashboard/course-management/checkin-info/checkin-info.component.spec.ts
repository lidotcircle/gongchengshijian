import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckinInfoComponent } from './checkin-info.component';

describe('CheckinInfoComponent', () => {
  let component: CheckinInfoComponent;
  let fixture: ComponentFixture<CheckinInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckinInfoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckinInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
