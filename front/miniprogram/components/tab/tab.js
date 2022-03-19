Component({
    /**
     * 组件的属性列表
     */
    properties: {
        tabList: {
            type: Array,
            value: []
        }
    },

    /**
     * 组件的方法列表
     */
    methods: {
        handleItemTap(e) {
            const {index} = e.currentTarget.dataset;
            this.triggerEvent("tabsItemChange", {index});
        }
    }
})
