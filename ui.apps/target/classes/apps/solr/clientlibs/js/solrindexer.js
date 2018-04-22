CQ.search = CQ.search || {};

CQ.search.SolrIndexer = CQ.Ext.extend(CQ.Ext.Viewport, {
    constructor : function(config) {
        this.results = document.createElement("iframe");
        this.results.id = "results_cq-solrindexer";
        this.results.name = "results_cq-solrindexer";
        this.results.height = "100%";
        this.results.width = "100%";
        this.results.onload = this.onResultsLoad;
        this.results.onreadystatechange = this.onResultsLoad;

        var importer = this;
        CQ.search.SolrIndexer.superclass.constructor.call(this, {
            "id" :"cq-solrindexer",
            "layout":"border",
            "renderTo":CQ.Util.getRoot(),
            "items" : [
                {
                    "id":"cq-solrindexer-wrapper",
                    "xtype":"panel",
                    "region":"center",
                    "layout":"border",
                    "border":false,
                    "items": [
                        {
                            "id":"cq-header",
                            "xtype":"container",
                            "autoEl":"div",
                            "region":"north",
                            "items": [
                                {
                                    "xtype":"panel",
                                    "border":false,
                                    "layout":"column",
                                    "cls": "cq-header-toolbar",
                                    "items": [
                                        new CQ.UserInfo({}),
                                        new CQ.HomeLink({})
                                    ]
                                }
                            ]
                        },{
                            "layout": "vbox",
                            "region": "center",
                            "items": [
                                {
                                    "xtype" :"form",
                                    "id" :"cq-solrindexer-form",
                                    "title":"Solr Indexer",									
	                                "region":"center",
                                    "standardSubmit" : true,
                                    "autoScroll": true,
                                    "border":false,
                                    "margins":"5 5 5 5",
                                    "autoHeight": true,
                                    "defaults" : {
                                        "anchor" : "-54"
                                    },
                                    "style" : "background-color:white",
                                    "bodyStyle" : "padding:10px", 
                                    "items" : [
                                        {
                                            "xtype" : "selection",
                                            "type" : "select",
                                            "fieldLabel" : "Indexing Options",
                                            "name" : "indexType",
                                            "options" : "/etc/solr/indexingoptions.json",
                                            "allowBlank" : false                                            
                                        }
                                    ],
                                    "buttonAlign":"left",
                                    "buttons":[
                                        {
                                            "id":"cq-solrindexer-btn-import",
                                            "text":CQ.I18n.getMessage("Index to Solr"),
                                            "handler":function() {
                                                var form = CQ.Ext.getCmp("cq-solrindexer-form").getForm();
                                                if (form.isValid()) {
                                                    var btn = CQ.Ext.getCmp("cq-solrindexer-btn-import");
                                                    btn.setDisabled(true);
                                                    var log = CQ.Ext.getCmp("cq-solrindexer-log");
                                                    log.expand();
                                                    // submit form
                                                    form.getEl().dom.action = "/bin/solr/push/pages";
                                                    form.getEl().dom.method = "GET";
                                                    form.getEl().dom.target = "results_cq-solrindexer";
                                                    form.submit();
                                                }
                                            }
                                        }
                                    ]
                                },
								{
                                    "xtype" :"form",
                                    "id" :"cq-solrindexer-delete-form",
                                    "title":"Clear indexes from Solr",									
	                                "region":"center",
                                    "standardSubmit" : true,
                                    "autoScroll": true,
                                    "border":false,
                                    "margins":"5 5 5 5",
                                    "autoHeight": true,
                                    "defaults" : {
                                        "anchor" : "-54"
                                    },
                                    "style" : "background-color:white",
                                    "bodyStyle" : "padding:10px",
                                    "buttonAlign":"left",
                                    "buttons":[
                                        {
                                            "id":"cq-solrindexer-delete-btn-import",
                                            "text":CQ.I18n.getMessage("Delete Indexes"),
                                            "handler":function() {
                                                var form = CQ.Ext.getCmp("cq-solrindexer-delete-form").getForm();
                                                if (form.isValid()) {
                                                    var btn = CQ.Ext.getCmp("cq-solrindexer-delete-btn-import");
                                                    btn.setDisabled(true);
                                                    var log = CQ.Ext.getCmp("cq-solrindexer-log");
                                                    log.expand();
                                                    // submit form
                                                    form.getEl().dom.action = "/bin/solr/delete/all/indexes";
                                                    form.getEl().dom.method = "POST";
                                                    form.getEl().dom.target = "results_cq-solrindexer";
                                                    form.submit();
                                                }
                                            }
                                        }
                                    ]                                    
                                }
                            ]
                        },{
                            "xtype":"panel",
                            "id" :"cq-solrindexer-log",
                            "region":"south",
                            "title":"Result Log",
                            "margins":"-5 5 5 5",
                            "height": 200,
                            "split":true,
                            "collapsible": true,
                            "collapsed": false,
                            "items":[
                                new CQ.Ext.BoxComponent({
                                    "autoEl": {
                                        "tag": "div"
                                    },
                                    "style": {
                                        "width": "100%",
                                        "height": "100%",
                                        "margin": "-2px"
                                    },
                                    "listeners":{
                                        render:function(wrapper) {
                                            new CQ.Ext.Element(importer.results).appendTo(wrapper.getEl());
                                        }
                                    }
                                })
                            ],
                            "plugins":[
                                {
                                    init: function(p) {
                                        if (p.collapsible) {
                                            var r = p.region;
                                            if ((r == "north") || (r == "south")) {
                                                p.on("collapse", function() {
                                                    var ct = p.ownerCt;
                                                    if (ct.layout[r].collapsedEl && !p.collapsedTitleEl) {
                                                        p.collapsedTitleEl = ct.layout[r].collapsedEl.createChild ({
                                                            tag:"span",
                                                            cls:"x-panel-collapsed-text",
                                                            html:p.title
                                                        });
                                                    }
                                                }, false, {single:true});
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        })
    },

    onResultsLoad: function() {
        var btnindex = CQ.Ext.getCmp("cq-solrindexer-btn-import");
        btnindex.setDisabled(false);  
		var btndelete = CQ.Ext.getCmp("cq-solrindexer-delete-btn-import");
        btndelete.setDisabled(false); 
    }
});
CQ.Ext.reg("solrforindexer", CQ.search.SolrIndexer);
