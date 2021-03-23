import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DaoyunComponent } from './daoyun.component';

describe('DaoyunComponent', () => {
  let component: DaoyunComponent;
  let fixture: ComponentFixture<DaoyunComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DaoyunComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DaoyunComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
