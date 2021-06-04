<template>
  <div class="sidebar-nav f-fl">
    <ul>
      <li>{{$t('user.Assets')}}</li>
      <li>
        <router-link to="/myAssets">{{$t('user.MyAssets')}}</router-link>
      </li>
      <li>
        <router-link to="/addressManage">{{$t('user.Address')}}</router-link>
      </li>
      <li>
        <router-link to="/assetsRecord">{{$t('user.Financial')}}</router-link>
      </li>
      <li>
        <router-link to="/tradeBill">{{$t('user.Bills')}}</router-link>
      </li>
    </ul>
  </div>
</template>


