
yachtApp.factory('ImageService', function() {

    /** All icons are prefixed with this */
    var iconPrefix = '/img/game-icons-net-png/icons/';

    var equipmentSlotIconsSmall = {
        'BODY': 'lorc/originals/breastplate.png',
        'CONSUMABLE': '',
        'HEAD': 'lorc/originals/barbute.png',
        'LEGS': 'irongamer/originals/armored-pants.png',
        'FEET': 'lorc/originals/boots.png',
        'HANDS': 'john-redman/hands/paper.png',
        'RING': 'lorc/originals/engagement-ring.png',
        'NECK': 'lorc/originals/gem-chain.png',
        'LEFT_HAND': '',
        'RIGHT_HAND': '',
    };

    var materialTypeIconsSmall = {
        'CLOTH': '/img/equipment_slot_icons/armor-vest.png',
        'HIDE': '/img/equipment_slot_icons/armor-vest.png',
        'WOOD': '/img/equipment_slot_icons/armor-vest.png',
        'METAL': '/img/equipment_slot_icons/armor-vest.png',
        'GEM': '/img/equipment_slot_icons/armor-vest.png',
        'FOOD': '/img/equipment_slot_icons/armor-vest.png',
        'MISC': '/img/equipment_slot_icons/armor-vest.png',
    };

    return {
        getEquipmentSlotIcon: function(equipmentSlot) {
            return iconPrefix + equipmentSlotIconsSmall[equipmentSlot];
        },
        getMaterialTypeIcon: function(materialType) {
            return materialTypeIconsSmall[materialType];
        },
    }
});
