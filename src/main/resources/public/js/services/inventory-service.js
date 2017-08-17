
yachtApp.factory('InventoryService', function($window, $location, $http, $log) {

    // finds the element in the list that has material.name equal to name
    var findInList = function(list, name) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].material.name == name) {
                return list[i];
            }
        }
        return null;
    };

    return {
        // subtracts quantity from the invMaterial, and returns a new invMaterial with quantity. If quantity is more than invMaterial's quantity, does nothing.
        splitItem: function(invMaterial, quantity) {
            if (invMaterial.quantity < quantity) {
                // nothing
                return;
            }
            // remove quantity from the item
            invMaterial.quantity -= quantity;

            // clone the item
            var newItem = JSON.parse(JSON.stringify(invMaterial));
            newItem.quantity = quantity;
            return newItem;
        },

        // adds the quantity of item2 to the quantity of item1
//        combineItem: function(item1, item2) {
//            item1.quantity += item2.quantity;
//        },

        // adds the item to the list. if it already exists in the list (by material.name), increase quantity of existing, otherwise push to list
        addToList: function(list, item) {
            var existingItem = findInList(list, item.material.name);
            if (existingItem == null) {
                list.push(item);
            }
            else {
                existingItem.quantity += item.quantity;
            }
        },

        // removes the items with name and quantity from the list. if the item has quantity remaining, it stays in the list.
        // if it's quantity remaining is 0 or less, removes it from the list
        removeFromList: function(list, name, quantity) {
            for (var i = 0; i < list.length; i++) {
                var mat = list[i];
                if (mat.material.name == name) {
                    mat.quantity -= quantity;       // reduce the quantity
                    if (mat.quantity <= 0) {
                        list.splice(i, 1);      // remove from array completely
                    }
                    return true;
                }
            }
            return false;
        },

        getEquipmentRequest: function(equipmentId, slot) {
            return {
                'equipmentId': equipmentId,
                'slot': slot
            };
        }
    }
});