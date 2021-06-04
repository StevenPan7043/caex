<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
          <asset-side-nav></asset-side-nav>
          <div class="content" id="view">
            <div class="inner-detail-lock" id="innerDetailLock">

              <div class="header">
                <a href="#/myLockup">{{$t('user.LockPosition')}}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{$t('user.DetailLock')}}</span>
              </div>

              <div class="lock-time" id="lockTime">

                <div class="lock-time-inner">
                  <span>{{$t('user.LockUpTime')}}</span>
                  <span class="time">
                    <span>{{recharge.r_create_time}}</span>
                  </span>
                </div>

                <div class="detail-boxs">
                  <div class="detail-box amount">
                    <div class="detail-box-inner">
                      <div class="row1 f-cb">
                        <i></i>
                        <span>{{$t('user.LockUpTheNumber')}}</span>
                      </div>
                      <div class="row2" id="amount">
                        <span>{{recharge.r_amount+' '+recharge.currency}}</span>
                      </div>
                    </div>
                  </div>
                  <div class="detail-box release">
                    <div class="detail-box-inner">
                      <div class="row1 f-cb">
                        <i></i>
                        <span>{{$t('user.HasBeenReleased')}}</span>
                      </div>
                      <div class="row2" id="release">
                        <span>{{recharge.r_release_num + ' ' + recharge.currency}}</span>
                      </div>
                    </div>
                  </div>
                  <div class="detail-box freeze">
                    <div class="detail-box-inner">
                      <div class="row1 f-cb">
                        <i></i>
                        <span>{{$t('user.LockUpFrozen')}}</span>
                      </div>
                      <div class="row2" id="freeze">
                        <span>{{recharge.r_unnum + ' ' + recharge.currency}}</span>
                      </div>
                    </div>
                  </div>
                  <!-- <div class="detail-box restime">
                            <div class="detail-box-inner">
                                <div class="row1 f-cb">
                                    <i></i>
                                    <span>剩余时间</span>
                                </div>
                                <div class="row2" id="restTime">
                                    <span>125天23时55分</span>
                                </div>
                            </div>
                        </div> -->
                </div>
              </div>
              <div class="detail-list" id="detailList">
                <div class="dl-title">{{$t('user.TheReleaseOfSubsidiary')}}</div>
                <table>
                  <thead>
                    <tr>
                      <th>{{$t('user.Time')}}</th>
                      <th>{{$t('user.Coin')}}</th>
                      <th>{{$t('user.BurstSize')}}</th>
                      <th>{{$t('user.Days')}}</th>
                      <th>{{$t('tradecenter.Status')}}</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item,index) in list" :key="index">
                      <td>
                       {{item.untime*1000|formatDate1}}' '{{item.untime*1000|formatDate2}}
                      </td>
                      <td>{{item.currency}}</td>
                      <td>{{item.u_num}}</td>
                      <td>{{item.qishu}}/60</td>
                      <td>{{$t('use.HasBeenReleased')}}</td>
                    </tr>
                  </tbody>
                </table>
                <div class="pages">
                  <div class="inner-pages f-cb">
                    <div class="flip f-fl">
                      <span class="prev">{{$t('user.PageUp')}}</span>
                      <span class="next">{{$t('user.PageDown')}}</span>
                    </div>
                    <div class="total f-fl">
                      {{$t('user.First')}}<span class="first"></span> {{$t('user.PpTotal')}}<span class="sum"></span> {{$t('user.Page')}}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import AssetSideNav from '@/components/AssetSideNav'
import { getLockRelease } from '@/api/detailLock'
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav
  },
  data () {
    return {
      id: '',
      currency: '',
      recharge: '',
      list: ''
    }
  },
  filters: {
    formatDate1 (ts) {
      let year = ts.getFullYear()
      let month = ts.getMonth()
      let day = ts.getDate()
      month = month + 1
      month = month < 10 ? '0' + month : month
      day = day < 10 ? '0' + day : day
      return year + '-' + month + '-' + day
    },

    formatDate2 (ts) {
      let hour = ts.getHours()
      let minute = ts.getMinutes()
      let second = ts.getSeconds()
      hour = hour < 10 ? '0' + hour : hour
      minute = minute < 10 ? '0' + minute : minute
      second = second < 10 ? '0' + second : second
      return hour + ' : ' + minute + ' : ' + second
    }
  },
  mounted () {
    this.id = this.$route.params.id
    this.currency = this.$route.params.currency
    getLockRelease(this.id).then(res => {
      if (res.status === 1) {
        this.recharge = res.recharge
        this.list = res.list
      }
    })
  },
  methods: {}
}
</script>
