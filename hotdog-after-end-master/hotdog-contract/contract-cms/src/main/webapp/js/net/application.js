window.onload = function() {
	var config = {
		id : "tg1",
		width : "800",
		renderTo : "div1",
		headerAlign : "left",
		headerHeight : "30",
		dataAlign : "left",
		indentation : "20",
		folderOpenIcon : "/images/folderOpen.png",
		folderCloseIcon : "/images/folderClose.png",
		defaultLeafIcon : "/images/defaultLeaf.gif",
		hoverRowBackground : "false",
		folderColumnIndex : "1",
		itemClick : "itemClickEvent",
		columns : [ {
			headerText : "级别",
			dataField : "levelname",
			headerAlign : "center",
			dataAlign : "center",
			width : "20"
		},{
			headerText : "会员信息",
			dataField : "name",
			headerAlign : "center",
			handler : "customOrgName"
		}, {
			headerText : "分区",
			dataField : "region",
			headerAlign : "center",
			dataAlign : "center",
			width : "100",
			hidden : false
		}, {
			headerText : "层级",
			dataField : "generation",
			headerAlign : "center",
			dataAlign : "center",
			width : "100",
			hidden : false
		}, {
			headerText : "查看",
			headerAlign : "center",
			dataAlign : "center",
			width : "50",
			handler : "customLook"
		} ],
		data : [ {
			  "cid": 1001,
			  "name": "CN10000",
			  "region": "B区",
			  "generation": 1,
			  "children": [
			    {
			      "cid": 25540,
			      "name": "MY31486108",
			      "region": "B区",
			      "generation": 2,
			      "children": [
			        
			      ]
			    },
			    {
			      "cid": 36218,
			      "name": "SG48416940",
			      "region": "A区",
			      "generation": 2,
			      "children": [
			        {
			          "cid": 2721,
			          "name": "SG38924737",
			          "region": "B区",
			          "generation": 3,
			          "children": [
			            {
			              "cid": 2722,
			              "name": "SG97344431",
			              "region": "A区",
			              "generation": 4,
			              "children": [
			                {
			                  "cid": 2724,
			                  "name": "SG23917969",
			                  "region": "A区",
			                  "generation": 5,
			                  "children": [
			                    
			                  ]
			                }
			              ]
			            }
			          ]
			        },
			        {
			          "cid": 10037,
			          "name": "Cn97323134",
			          "region": "A区",
			          "generation": 3,
			          "children": [
			            {
			              "cid": 3955,
			              "name": "MY24108812",
			              "region": "A区",
			              "generation": 29,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 10038,
			              "name": "CN38835100",
			              "region": "B区",
			              "generation": 4,
			              "children": [
			                {
			                  "cid": 10039,
			                  "name": "CN83679376",
			                  "region": "B区",
			                  "generation": 5,
			                  "children": [
			                    
			                  ]
			                },
			                {
			                  "cid": 10040,
			                  "name": "CN11741694",
			                  "region": "A区",
			                  "generation": 5,
			                  "children": [
			                    
			                  ]
			                }
			              ]
			            },
			            {
			              "cid": 10049,
			              "name": "SG71699983",
			              "region": "A区",
			              "generation": 4,
			              "children": [
			                {
			                  "cid": 10050,
			                  "name": "cn55694991",
			                  "region": "A区",
			                  "generation": 5,
			                  "children": [
			                    
			                  ]
			                },
			                {
			                  "cid": 10052,
			                  "name": "SG86363739",
			                  "region": "B区",
			                  "generation": 5,
			                  "children": [
			                    
			                  ]
			                },
			                {
			                  "cid": 10053,
			                  "name": "SG21495017",
			                  "region": "A区",
			                  "generation": 6,
			                  "children": [
			                    
			                  ]
			                }
			              ]
			            },
			            {
			              "cid": 24098,
			              "name": "CN26384335",
			              "region": "A区",
			              "generation": 45,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 26328,
			              "name": "SG84994764",
			              "region": "A区",
			              "generation": 48,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 26464,
			              "name": "SG57843827",
			              "region": "A区",
			              "generation": 60,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 27493,
			              "name": "CN49061301",
			              "region": "A区",
			              "generation": 52,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 32469,
			              "name": "CN51147286",
			              "region": "A区",
			              "generation": 49,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 38364,
			              "name": "SG89431183",
			              "region": "B区",
			              "generation": 67,
			              "children": [
			                
			              ]
			            },
			            {
			              "cid": 38367,
			              "name": "CN56546556",
			              "region": "B区",
			              "generation": 43,
			              "children": [
			                
			              ]
			            }
			          ]
			        }
			      ]
			    }
			  ]
			}]
	};
	debugger;
	var treeGrid = new TreeGrid(config);
	treeGrid.show()
}