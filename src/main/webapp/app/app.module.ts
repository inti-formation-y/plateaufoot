import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PlateaufootSharedModule } from 'app/shared/shared.module';
import { PlateaufootCoreModule } from 'app/core/core.module';
import { PlateaufootAppRoutingModule } from './app-routing.module';
import { PlateaufootHomeModule } from './home/home.module';
import { PlateaufootEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PlateaufootSharedModule,
    PlateaufootCoreModule,
    PlateaufootHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PlateaufootEntityModule,
    PlateaufootAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class PlateaufootAppModule {}
