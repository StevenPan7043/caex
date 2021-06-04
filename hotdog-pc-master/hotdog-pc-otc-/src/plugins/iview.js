import Vue from 'vue';
import {
  Button,
  Select,
  Option,
  Page,
  Dropdown,
  DropdownMenu,
  DropdownItem,
  Avatar,
  Input,
  Form,
  FormItem,
  Checkbox,
  Message,
  Spin,
  Icon,
  Collapse,
  Panel,
  Modal,
  Upload,
  CheckboxGroup,
  Radio,
  RadioGroup,
  Scroll,
  Poptip,
  Badge,
} from 'iview';

import 'iview/dist/styles/iview.css';
import { Notice } from 'iview';

Vue.prototype.$Message = Message;
Vue.prototype.$Modal = Modal;
Vue.prototype.$Notice = Notice;

Vue.component('Button', Button);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Page', Page);
Vue.component('Dropdown', Dropdown);
Vue.component('DropdownMenu', DropdownMenu);
Vue.component('DropdownItem', DropdownItem);
Vue.component('Avatar', Avatar);
Vue.component('Input', Input);
Vue.component('Form', Form);
Vue.component('FormItem', FormItem);
Vue.component('Checkbox', Checkbox);
Vue.component('Spin', Spin);
Vue.component('Icon', Icon);
Vue.component('Collapse', Collapse);
Vue.component('Panel', Panel);
Vue.component('Modal', Modal);
Vue.component('Upload', Upload);
Vue.component('CheckboxGroup', CheckboxGroup);
Vue.component('Radio', Radio);
Vue.component('RadioGroup', RadioGroup);
Vue.component('Scroll', Scroll);
Vue.component('Poptip', Poptip);
Vue.component('Badge', Badge);
