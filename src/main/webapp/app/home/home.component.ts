import { Component, OnInit } from '@angular/core';

import { LoginService } from 'app/core/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { TableOfPrizesService } from 'app/entities/table-of-prizes/table-of-prizes.service';
import { ITableOfPrizes } from 'app/shared/model/table-of-prizes.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit {
  account: Account | null = null;
  tablesOfPrizes?: ITableOfPrizes[];

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private tableOfPrizesService: TableOfPrizesService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.account = account));
    this.tableOfPrizesService.recent().subscribe(res => (this.tablesOfPrizes = res.body || []));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginService.login();
  }
}
