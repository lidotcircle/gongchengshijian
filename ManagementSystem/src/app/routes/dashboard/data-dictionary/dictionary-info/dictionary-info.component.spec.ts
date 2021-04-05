import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DictionaryInfoComponent } from './dictionary-info.component';

describe('DictionaryInfoComponent', () => {
  let component: DictionaryInfoComponent;
  let fixture: ComponentFixture<DictionaryInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DictionaryInfoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DictionaryInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
