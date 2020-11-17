
var effect;

var world = {
    landmarks: [],
    latents: []
};

function configure(newEffect) {
    effect = newEffect;
    effect.init();
}

function onFRXUpdate(strLandmarks, strLatents) {
    world.landmarks = strLandmarks ? strLandmarks : [];
    world.latents = strLatents ? strLatents : [];

    for (var i = 0; i < effect.faceActions.length; i++) {
        effect.faceActions[i]();
    }
}

function onUpdate() {
    for (var i = 0; i < effect.noFaceActions.length; i++) {
        effect.noFaceActions[i]();
    }
}

function onVideoRecordStart() {
    var i;
    for (i = 0; i < effect.videoRecordStartActions.length; i++) {
        effect.videoRecordStartActions[i]();
    }
}

function onVideoRecordFinish() {
    var i;
    for (i = 0; i < effect.videoRecordFinishActions.length; i++) {
        effect.videoRecordFinishActions[i]();
    }
}

function onVideoRecordDiscard() {
    var i;
    for (i = 0; i < effect.videoRecordDiscardActions.length; i++) {
        effect.videoRecordDiscardActions[i]();
    }
}

// Based on https://banuba.atlassian.net/wiki/pages/viewpage.action?pageId=31719449

switch(Api.getFRXVersion())
{
case 31:
    Api.print("Detected FRX version: 31");

    isMouthOpen = function (landmarks,latents) {
        if(latents.length > 1) {
            return (latents[0] > 0.7 && latents[2] > -0.1) ;
        } else {
            return false;
        }
    };

    isSmile = function(landmarks,latents) {
        if(latents.length > 4) {
           return (latents[1] > 0.7 && latents[5] > 0.5);
        } else {
            return false;
        }
    };

    isEyebrowUp = function(landmarks,latents) { return false; }
    isDisgust = function(landmarks, latents) { return false; }

    break;
case 3:
    Api.print("Detected FRX version: 3");

    isMouthOpen = function (landmarks,latents) {
        if(latents.length > 1) {
            return latents[0] < 0 && latents[1] > 1.45;
        } else {
            return false;
        }
    };

    isSmile = function(landmarks,latents) {
        if(latents.length > 4) {
            return (latents[0] > 0 && latents[3] > 1.45);
        } else {
            return false;
        }
    };

    isEyebrowUp = function(landmarks,latents) { return false; }
    isDisgust = function(landmarks, latents) { return false; }

    break;
case 2:
    isMouthOpen = function (landmarks,latents) {
        if(latents.length > 0) {
            return latents[0] > 1;
        } else {
            return false;
        }
    };

    isSmile = function(landmarks,latents) {
        if(latents.length > 5) {
            return latents[5] > 0 && latents[3] > 1;
        } else {
            return false;
        }
    };

    isEyebrowUp = function(landmarks,latents) {
        if(latents.length > 7) {
            return latents[7] < -1;
        } else {
            return false;
        }
    };

    isDisgust = function(landmarks, latents) {
        if(latents.length > 17) {
            return (latents[17] > 1.5);
        } else {
            return false;
        }
    };

    break;
default:
    Api.print("Error: unknown FRX version");
}

//
// Special case during migration from FRX2 to FRX3.
// There is no isEyebrowUp trigger in FRX3 yet, so we change it to smile for a while.
//

if(Api.getFRXVersion() >= 3)
{
    Api.print("Chosing isSmile function for isEyebrowUp trigger (FRX3 migration)");
    isEyebrowUpSpecialFunction = function(landmarks,latents) {
        return isSmile(landmarks, latents);
    };

    isEyebrowUpSpecialHint = "Smile";
}
else if(Api.getFRXVersion() == 2)
{
    Api.print("Chosing isEyebrowUp function for isEyebrowUp trigger (FRX2)");
    isEyebrowUpSpecialFunction = function(landmarks,latents) {
        return isEyebrowUp(landmarks, latents);
    };

    isEyebrowUpSpecialHint = "Raise eyebrows";
}

// Duktape on Android doesn't support function overloading.
// We do it on JS side for Api.meshfxMsg.

if(Api.getPlatform() == "android")
{
    Api.native_meshfxMsg = Api.meshfxMsg;
    Api.meshfxMsg = function (param1, param2, param3, param4) {
        if(param3 == undefined && param4 == undefined) {
            Api.native_meshfxMsg(param1, param2, 0, "");
        } else if(param4 == undefined) {
            Api.native_meshfxMsg(param1, param3, param3, "");
        } else {
            Api.native_meshfxMsg(param1, param2, param3, param4);
        }
    }
}
