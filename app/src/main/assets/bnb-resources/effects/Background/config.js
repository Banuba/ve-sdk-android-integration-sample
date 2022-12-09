require("bnb_js/timers.js")

Background = require('bnb_js/background');
Background.contentMode("fill")
let endTime = 0;
let interval;
let isEnded = false;
let isPaused = false;

function setBgTexture(path){
    Background.texture(path);
}

function setBgVideo(path){
    Background.texture(path, false, true);

}

function setBgTextureByFd(fd){
    Background.textureImageByFd(fd);
}

function setBgVideoByFd(fd){
    Background.textureVideoByFd(fd, false, true);
}

function rotateBg(angle){
    Background.rotation(angle);
}

function playVideo(){
    Background.getBackgroundVideo().asMedia().play();
}

function resumeVideo(){
    Background.getBackgroundVideo().asMedia().resume();
    isPaused = false;
}

function playVideoRange(start, end){
    const BG = Background.getBackgroundVideo().asMedia();
    isEnded = false;
    BG.setLooped(false);
    BG.setStartPosition(start);
    BG.setEndPosition(end);
    BG.play();
        interval = setInterval(()=>{
            bnb.log(BG.isPlaying())
            if(BG.isPlaying() == false && isPaused == false){
                BG.setStartPosition(0);
                BG.setLooped(true);
                BG.play();
                clearInterval(interval);
                isEnded = true;
                bnb.log("clear")
            }
        },100)
}

function updatePreview(time, delay){
    endTime = time;
    Background.getBackgroundVideo().asMedia().setStartPosition(endTime);
    Background.getBackgroundVideo().asMedia().setEndPosition(endTime);
    Background.getBackgroundVideo().asMedia().play();
    setTimeout(()=>{
        Background.getBackgroundVideo().asMedia().pause();
    },delay)
}

function stopVideo(){
    clearInterval(interval);
    setTimeout(()=>{
        Background.getBackgroundVideo().asMedia().stop();
    }, 100)
}

function pauseVideo(){
    bnb.log(isEnded)
    isPaused = true;
    isEnded ?? clearInterval(interval);
    setTimeout(()=>{
        Background.getBackgroundVideo().asMedia().pause();
    }, 100)
}
setBgTexture("images/default_tex.png")