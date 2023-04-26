import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {CityDto} from "./model/city.dto";
import {ApiService} from "./api/api.service";
import {PageDto} from "./model/page.dto";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {AuthenticationService} from "./service/authentication.service";
import {MatDialog} from "@angular/material/dialog";
import {LoginDialogComponent} from "./components/login-dialog/login-dialog.component";
import {Roles} from "./enums/roles";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'city-explorer-fe';

  dataSource = new MatTableDataSource<CityDto>();
  displayedColumns: string[] = ["id", "name", "photoLink", "photoImage", "saveButton"];
  totalRows = 0;
  totalPages = 0;
  isLoading = false;
  pageSize = 5;
  currentPage = 0;
  pageSizeOptions: number[] = [5, 10, 25, 100];
  disabled = true;
  authorized = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  username: string = '';
  canEdit: boolean = false;

  constructor(private apiService: ApiService,
              private authenticationService: AuthenticationService,
              public dialog: MatDialog) {
    window.addEventListener("beforeunload", function(e) {
      authenticationService.logout();
    });
  }

  ngOnInit(): void {
    this.disabled = this.authenticationService.isUserLoggedIn();
    this.loadData();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  loadData(name?: string) {
    this.isLoading = true;
    const pageSearch = name ? {
      'name': name,
      'page': this.currentPage,
      'size': this.pageSize
    } : {
      'page': this.currentPage,
      'size': this.pageSize
    }

    this.apiService.get<PageDto>("cities", pageSearch)
      .toPromise()
      .then(response => {
        this.totalPages = response?.totalPages ?? 0;
        this.totalRows = response?.totalElements ?? 0;
        return response?.content;
      })
      .then(data => {
        this.dataSource.data = data ?? [];
        setTimeout(() => {
          this.paginator.pageIndex = this.currentPage;
          this.paginator.length = this.totalRows;
        });
        this.isLoading = false;
      }, error => {
        console.log(error);
        this.isLoading = false;
      });
  }

  pageChanged(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.loadData();
  }

  saveValue(element: CityDto) {
    this.apiService.patch<CityDto>("cities", {
      id: element.id,
      name: element.name,
      photoLink: element.photoLink
    }).subscribe(data => {
      element = data;
    });
  }

  openLoginDialog() {
    this.dialog.open(LoginDialogComponent).afterClosed().subscribe(result => {
      this.username = sessionStorage.getItem('username') ?? '';
      this.authorized = result;
      this.canEdit = this.authenticationService.hasRole(Roles.ROLE_ALLOW_EDIT);
    });
  }

  logout() {
    this.authenticationService.logout();
    this.authorized = this.authenticationService.isUserLoggedIn();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.loadData(filterValue.trim());
    // this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
