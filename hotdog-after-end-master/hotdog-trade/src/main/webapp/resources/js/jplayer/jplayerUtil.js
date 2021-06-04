var voiceType = 1;

$(function(){
	$("#jquery_jplayer").jPlayer({
		swfPath:"resources/js/jplayer"
	});
	var voicePlayTimes = 0;
	
	var inOrOutVoicePlayTimes = 0;

	$("#jquery_jplayer").onSoundComplete(function () {
		if (voiceType == 1) {
			playListNext();
		} else if (voiceType == 11) {
			playVehicleInOrOutListNext();
		}

	});

	function playListChange( src ,obj) {
		voicePlayTimes++;
		//console.log(voicePlayTimes);
		if (voicePlayTimes<13) {
			$("#jquery_jplayer").changeAndPlay(src);
		}else {
			voicePlayTimes = 0;
		}
	}

	
	var playItem = 0;
	var myPlayListLength = $(".playlist_content li").length;
	function playListNext() {
		var currentIndex = $(".playlist_content li").index($(".playlist_current"));
		playItem = currentIndex;
		var index = (playItem+1 < myPlayListLength) ? playItem+1 : 0;
		var $liindex = $(".playlist_content li:eq("+index+")") ;
		var playListSrc= $liindex.attr("jplayer");
		playListChange(playListSrc , $liindex );//播放mp3
		$liindex.addClass("playlist_current").siblings().removeClass("playlist_current");
	}
	
	
	function playVehicleInOrOutListNext() {
		var currentIndex = $(".playlist_content li").index($(".playlist_current"));
		playItem = currentIndex;
		var index = (playItem+1 < myPlayListLength) ? playItem+1 : 0;
		var $liindex = $(".playlist_content li:eq("+index+")") ;
		var playListSrc= $liindex.attr("jplayer");
		playVehicleInOrOut(playListSrc , $liindex );//播放mp3
		$liindex.addClass("playlist_current").siblings().removeClass("playlist_current");
	}

	function play_voice(typelink) {
		voiceType = 1;
		var txt_voice = $('#voice_vehicle').val();
		if (txt_voice!=''&&txt_voice>0) {
			txt_voice = parseInt(txt_voice,10);
			var baiwei = 0;
			var shiwei = 0;
			var gewei = 0;
			
			baiwei = parseInt(txt_voice / 100, 10);	
			shiwei = parseInt((txt_voice-baiwei*100)/10, 10);
			gewei =  parseInt(txt_voice % 10, 10);
			
			voicePlayTimes = 0;
			$('.play_type').attr('jplayer',typelink);
			$('.player_num1').attr('jplayer','resources/voice/'+baiwei+'.mp3');
			$('.player_num2').attr('jplayer','resources/voice/'+shiwei+'.mp3');
			$('.player_num3').attr('jplayer','resources/voice/'+gewei+'.mp3');
			var $liindex = $(".playlist_content li:eq(0)");
			var playListSrc= $liindex.attr("jplayer");
			playListChange(playListSrc , $liindex );//播放mp3
			$liindex.addClass("playlist_current").siblings().removeClass("playlist_current");
		}else {
			$('.txt_voice').focus();
		}
	}	
	

	var autoVoice = {
		autoV:null,
		palyTip : function () {
			voiceType = 2;
			var $liindex = $(".playlist_tip li:eq(0)");
			var playListSrc= $liindex.attr("jplaytip");
			$("#jquery_jplayer").changeAndPlay(playListSrc);
		},
		autoVoiceTip : function () {
			clearInterval(this.autoV);
			this.autoV = setInterval(function () {
				if ($('#gridBoxgrid .l-grid-body-table .l-grid-row').length>0) {
					autoVoice.palyTip();
					//console.log(autoVoice.autoV);
				}
			},10000);
		},
		labClick : function () {
			if ($('#chk_voicetip:checked').length) {
				//this.autoVoiceTip();
			}else {
				//console.log(this.autoV);
				//clearInterval(this.autoV);
			}
		},
		init : function () {
			/*if ($('#chk_voicetip:checked').length) {
				this.autoVoiceTip();
				if ($('#gridBoxgrid .l-grid-body-table .l-grid-row').length) {
					this.palyTip();
				}
			}
			$('.lab_voiceTip').bind('click',function () {
				autoVoice.labClick();
			});*/
		}
	}


	if ($('#chk_voicetip').length) {
		autoVoice.init();
	}

	$('.sss').click(function () {
		palyTip();
	});

	//暂停后 播放音乐
	$("#player_play a").click(function(){
		$('#jquery_jplayer').play(); 
		return false;
	});
	//暂停音乐
	$("#jplayerStop").click(function(){
		$('#jquery_jplayer').pause();
		voicePlayTimes = 0;
		return false;
	});

	/*播放*/
	$("#jplayerReaday").click(function(){
		play_voice('resources/voice/zuozhunbei.mp3');
		return false;
	});
	$("#jplayerIn").click(function(){
		play_voice('resources/voice/jinzhan.mp3');
		return false;
	});
	
	 $.vehicleOut = function(vno){
		play_vehicle_out_in('resources/voice/chuzhan.mp3', vno);
		return false;
	}
	$.vehicleIn = function (vno){
		play_vehicle_out_in('resources/voice/huizhan.mp3', vno);
		return false;
	}
	
	
	function play_vehicle_out_in(typelink, vno) {
		voiceType = 11;
		var txt_voice = vno;
		if (txt_voice!=''&&txt_voice>0) {
			txt_voice = parseInt(txt_voice,10);
			var baiwei = 0;
			var shiwei = 0;
			var gewei = 0;
			
			baiwei = parseInt(txt_voice / 100, 10);	
			shiwei = parseInt((txt_voice-baiwei*100)/10, 10);
			gewei =  parseInt(txt_voice % 10, 10);
			
			inOrOutVoicePlayTimes = 0;
			$('.play_type').attr('jplayer',typelink);
			$('.player_num1').attr('jplayer','resources/voice/'+baiwei+'.mp3');
			$('.player_num2').attr('jplayer','resources/voice/'+shiwei+'.mp3');
			$('.player_num3').attr('jplayer','resources/voice/'+gewei+'.mp3');
			var $liindex = $(".playlist_content li:eq(0)");
			var playListSrc= $liindex.attr("jplayer");
			playVehicleInOrOut(playListSrc , $liindex );//播放mp3
			$liindex.addClass("playlist_current").siblings().removeClass("playlist_current");
		}
	}
	function playVehicleInOrOut( src ,obj) {
		inOrOutVoicePlayTimes++;
		//console.log(voicePlayTimes);
		if (inOrOutVoicePlayTimes<6) {
			$("#jquery_jplayer").changeAndPlay(src);
		}else {
			inOrOutVoicePlayTimes = 0;
		}
	}

 
});

function palyTipForOnce() {
	voiceType = 2;
	var $liindex = $(".playlist_tip li:eq(0)");
	var playListSrc= $liindex.attr("jplaytip");
	$("#jquery_jplayer").changeAndPlay(playListSrc);
}