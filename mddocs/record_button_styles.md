# Banuba AI Video Editor SDK
## Record button styles

To configure appearance and behavior of the record button just use `recordButtonStyle` attribute of the main theme with the following parameters:

**recordButtonRecordingSize** - for the size of recording button

**recordButtonInnerIdleDrawable** - for the drawable inside record button shown during idle state

**recordButtonInnerRecordingDrawable** - for the drawable inside record button shown during recording

**recordButtonElevation** - elevation of the record button

**recordButtonInnerRecordingSize** - size of the drawable shown during recording

**recordButtonCircleIdleWidth** - width of the record button circle during idle state

**recordButtonCircleRecordingWidth** - width of the record button circle during recording

**recordButtonCircleGradientColors** - array for the gradient colors that fill the record button circle during recording

**recordButtonCircleSolidColor** - color of the record button circle

**recordButtonAnimationPhotoScale** - array with two integers where the first is an initial scale and the second is a final scale during animation (these values should be multiplier of 1000: for example, for 1.0 use 1000)

**recordButtonAnimationVideoInnerScale** - array with two integers where the first is an initial scale of the inner drawable of record button and the second is a final scale during animation (these values should be multiplier of 1000: for example, for 1.0 use 1000)

**recordButtonAnimationVideoOuterScale** - array with two integers where the first is an initial scale of the outer circle of record button and the second is a final scale during animation (these values should be multiplier of 1000: for example, for 1.0 use 1000)

**recordButtonAnimationDuration** - duration for all animations of the record button

![img](screenshots/recordButton.png)
