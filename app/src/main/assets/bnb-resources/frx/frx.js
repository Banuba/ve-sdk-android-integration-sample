
var effect;

var world = {
    landmarks: [],
    latents: []
};

function configure(newEffect)
{
    effect = newEffect;
    effect.init();
}


function onFRXUpdate(strLandmarks, strLatents)
{
    world.landmarks = strLandmarks ? strLandmarks : [];
    world.latents = strLatents ? strLatents : [];

    for (var i = 0; i < effect.faceActions.length; i++) {
        effect.faceActions[i]();
    }
}

function onUpdate()
{
    for (var i = 0; i < effect.noFaceActions.length; i++) {
        effect.noFaceActions[i]();
    }
}

function onVideoRecordStart()
{
    var i;
    for (i = 0; i < effect.videoRecordStartActions.length; i++) {
        effect.videoRecordStartActions[i]();
    }
}

function onVideoRecordFinish()
{
    var i;
    for (i = 0; i < effect.videoRecordFinishActions.length; i++) {
        effect.videoRecordFinishActions[i]();
    }
}

function onVideoRecordDiscard()
{
    var i;
    for (i = 0; i < effect.videoRecordDiscardActions.length; i++) {
        effect.videoRecordDiscardActions[i]();
    }
}


isMouthOpen = Api.isMouthOpen;
isSmile = Api.isSmile;
isEyebrowUp = Api.isEyebrowUp;
isDisgust = Api.isDisgust;
getEyesStatus = Api.getEyesStatus;

//too many false positives for now
//isEyebrowUpSpecialFunction = isEyebrowUp;
//isEyebrowUpSpecialHint = "Raise eyebrows";
isEyebrowUpSpecialFunction = isSmile;
isEyebrowUpSpecialHint = "Smile";


function jsonToApi(json, api)
{
    var obj = JSON.parse(json)
    for (var key in api)
    {
        var methodName = key
        var propertyName = api[key] 
        if (obj.hasOwnProperty(propertyName))
        {
            Api[methodName](obj[propertyName])
        }
    }
}

// Duktape on Android doesn't support function overloading.
// We do it on JS side for Api.meshfxMsg.

if (Api.getPlatform() == "android") {
    Api.native_meshfxMsg = Api.meshfxMsg;
    Api.meshfxMsg = function(param1, param2, param3, param4) {
        if (param3 == undefined && param4 == undefined) {
            Api.native_meshfxMsg(param1, param2, 0, "");
        } else if (param4 == undefined) {
            Api.native_meshfxMsg(param1, param2, param3, "");
        } else {
            Api.native_meshfxMsg(param1, param2, param3, param4);
        }
    }
}

if (Api.getCurrentTimeNs() != 0) {
    Date.prototype.getTime = function() {
        return Api.getCurrentTimeNs() * 1e-6;
    } 
    Api.print("Using overriding Date.getTime() implementation.");
    Api.print("getTime() value is: " + (new Date()).getTime());
}
