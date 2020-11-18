
function Effect()
{
    var self = this;

    this.init = function() {
        Api.meshfxMsg("spawn", 1, 0, "!glfx_FACE");
        Api.meshfxMsg("spawn", 0, 0, "quad.bsm2");
        Api.meshfxMsg("shaderVec4", 0, 0, "1.0 0.0 0.2417 0.9917");
        Api.showRecordButton();
        // self.faceActions = [];
    };

    this.faceActions = [function(){ Api.meshfxMsg("shaderVec4", 0, 1, "1."); }];
    this.noFaceActions = [function(){ Api.meshfxMsg("shaderVec4", 0, 1, "0."); }];

    this.videoRecordStartActions = [];
    this.videoRecordFinishActions = [];
    this.videoRecordDiscardActions = [];
}

function setColor(color)
{
    var c = JSON.parse(color);
    Api.meshfxMsg("shaderVec4", 0, 0, c[0] + " " + c[1] + " " + c[2] + " " + c[3]);
}
configure(new Effect());
