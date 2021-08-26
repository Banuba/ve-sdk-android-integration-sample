function Effect()
{
    this.init = function() {
        // spawning model (plane) for bg separation
        Api.meshfxMsg("spawn", 0, 0, "tri.bsm2"); 
        setBgTexture("default_tex.png");
    };
    this.faceActions = [];
    this.noFaceActions = [];

    this.videoRecordStartActions = [];
    this.videoRecordFinishActions = [];
    this.videoRecordDiscardActions = [];
}

function setBgTexture(fileName) {
    rotateBg(0);
    Api.meshfxMsg("shaderVec4", 0, 0, "1 0 0 0");
    Api.meshfxMsg("tex", 0, 0, fileName);

    Api.print("setBgTexture - texture " + fileName + " has been set");

}

function setBgVideo(fileName) {
    rotateBg(0);
    Api.meshfxMsg("shaderVec4", 0, 0, "0 0 0 0");
    Api.setVideoFile("frx", fileName);
    Api.playVideoRange("frx", 0.01, 0.02, false, 1);

    Api.print("setBgVideo - video file " + fileName + " has been set");
}

function playVideo() {
    Api.print("playVideo - video is playing.");
    Api.playVideo("frx", true, 1);
}

function stopVideo() {
    Api.print("stopVideo - video has been stopped.");
    Api.stopVideo("frx");
}

function pauseVideo() {
    Api.print("pauseVideo - video is on a pause.");
    Api.pauseVideo("frx");
}

function rotateBg(angle) {
    var anglesAvailable = [0,90,180,270,-270,-90];
    if(!contains(anglesAvailable,parseInt(angle))) {
        Api.print("rotateBgTex(angle) - unable to set any angle except the following [0,90,-90,270,-270,180]");
        return;
    }
    Api.print("rotateBgTex - angle " + angle + "degrees is set");
    Api.meshfxMsg("shaderVec4", 0, 1, angle + " 0.0 0.0 0.");
}

function contains(a, obj) {
    var i = a.length;
    while (i--) {
       if (a[i] == obj) {
           return true;
       }
    }
    return false;
}

configure(new Effect());

function timeOut(delay, callback) {
    var timer = new Date().getTime();

    effect.faceActions.push(removeAfterTimeOut);
    effect.noFaceActions.push(removeAfterTimeOut);

    function removeAfterTimeOut() {
        var now = new Date().getTime();

        if (now >= timer + delay) {
            var idx = effect.faceActions.indexOf(removeAfterTimeOut);
            effect.faceActions.splice(idx, 1);
            idx = effect.noFaceActions.indexOf(removeAfterTimeOut);
            effect.noFaceActions.splice(idx, 1);
            callback();
        }
    }
}