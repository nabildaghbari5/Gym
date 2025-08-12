import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Page route
import { DashboardsRoutingModule } from './dashboards-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';

// Simplebar
import { SimplebarAngularModule } from 'simplebar-angular';

// Count To
import { CountUpDirective, CountUpModule } from 'ngx-countup';

// Flat Picker
import { FlatpickrModule } from 'angularx-flatpickr';

// Apex Chart Package
import { NgApexchartsModule } from 'ng-apexcharts';
import { NgxEchartsModule } from 'ngx-echarts';
import * as echarts from 'echarts';

import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

// Leaflet map
import { LeafletModule } from '@asymmetrik/ngx-leaflet';

// component
import { AnalyticsComponent } from './analytics/analytics.component';
import { CrmComponent } from './crm/crm.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgSelectModule } from '@ng-select/ng-select';
import { HistoriqueComponent } from './historique/historique.component';

@NgModule({
  declarations: [
    AnalyticsComponent,
    CrmComponent,
    HistoriqueComponent

  ],
  imports: [
    CommonModule,
    DashboardsRoutingModule,
    SharedModule,
    NgSelectModule ,
    BsDropdownModule,
    CountUpModule,
    NgApexchartsModule,
    TabsModule.forRoot(),
    TooltipModule.forRoot(),
    PaginationModule.forRoot(),
    FormsModule,
    ReactiveFormsModule,
    SimplebarAngularModule,
    ProgressbarModule.forRoot(),
    LeafletModule,
    NgxEchartsModule.forRoot({ echarts }),
    ModalModule.forRoot(),
    BsDatepickerModule.forRoot(),
    FlatpickrModule.forRoot(),
    

  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DashboardsModule { }
