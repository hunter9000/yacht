
yachtApp.factory('ItemService', function() {

    var equipmentSlots = ['CONSUMABLE','HEAD','BODY','LEGS','FEET','HANDS','RING','NECK','LEFT_HAND','RIGHT_HAND'];

    var materialTypes = ['CLOTH', 'HIDE', 'WOOD', 'METAL', 'GEM', 'FOOD', 'MISC'];

    return {
        getEquipmentSlots: function() {
            return equipmentSlots.slice();      // return copy of array
        },
        getMaterialTypes: function() {
            return materialTypes.slice();
        },
    }
});


