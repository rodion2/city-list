import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {CityDto} from "./model/city.dto";
import {ApiService} from "./api/api.service";
import {PageDto} from "./model/page.dto";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {AuthenticationService} from "./service/authentication.service";

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
  editable = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  username: string = "";
  password: string = "";

  constructor(private apiService: ApiService,
              private authenticationService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  loadData() {
    this.isLoading = true;
    const pageSearch = {
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
    console.log(element);
    this.apiService.put<CityDto>("cities", {
      id: element.id,
      name: element.name,
      photoLink: element.photoLink
    }, {headers: {authorization: 'Basic ' + window.btoa(this.username + ":" + this.password)}}).subscribe(data => {
      element = data;
    });
  }

  login(username: string, password: string) {
    console.log(this.username, this.password);
    this.authenticationService.login({username, password}).toPromise().then((data) => {
      this.editable = true;
    }, error => {
      console.log("Authentidication failed.", error);
    });
  }

  logout() {
    this.authenticationService.logout();
  }
}
