<template>
  <div class="sidebar-nav f-fl">
    <ul>
      <li>{{$t('user.MyCenter')}}</li>
      <li>
        <router-link to="/accountInfo">{{$t('user.AccountInfo')}}</router-link>
      </li>
      <li>
        <router-link to="/identityVerify">{{$t('user.Certification')}}</router-link>
      </li>
      <li>
        <router-link to="/accountSecurity">{{$t('user.AccountSecurity')}}</router-link>
      </li>
      <li>
        <router-link to="/loginRecord">{{$t('user.LoginRecord')}}</router-link>
      </li>
      <li>
        <router-link to="/apiManager">{{$t('user.APIManagement')}}</router-link>
      </li>
    </ul>
  </div>
</template>
