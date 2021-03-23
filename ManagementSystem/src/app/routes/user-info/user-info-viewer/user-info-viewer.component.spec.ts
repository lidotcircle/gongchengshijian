import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserInfoViewerComponent } from './user-info-viewer.component';

describe('UserInfoViewerComponent', () => {
  let component: UserInfoViewerComponent;
  let fixture: ComponentFixture<UserInfoViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserInfoViewerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserInfoViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
