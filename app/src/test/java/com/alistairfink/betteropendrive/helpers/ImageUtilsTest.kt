package com.alistairfink.betteropendrive.helpers

import android.graphics.BitmapFactory
import android.util.Base64
import org.junit.Assert
import org.junit.Test

class ImageUtilsTest
{
    @Test
    fun getCircularBitmapTest()
    {
        // FIXME : This needs to be mocked
        var bitmapString: String = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAUDBAQEAwUEBAQFBQUGBwwIBwcHBw8LCwkMEQ8SEhEP\\nERETFhwXExQaFRERGCEYGh0dHx8fExciJCIeJBweHx7/2wBDAQUFBQcGBw4ICA4eFBEUHh4eHh4e\\nHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh7/wAARCABkAGQDASIA\\nAhEBAxEB/8QAHAAAAQQDAQAAAAAAAAAAAAAABgAEBQcCAwgB/8QAOBAAAgICAQMDAgQFAQcFAAAA\\nAQIDBAURBgASIQcTMSJBFDJRYQgVI3GBFiQlQkNSYqEzkZKiwf/EABsBAAEFAQEAAAAAAAAAAAAA\\nAAEAAgMFBgQH/8QAKhEAAQMDAwMEAgMBAAAAAAAAAQACEQMhMQQSQQUiUQYycYETYUKRoVL/2gAM\\nAwEAAhEDEQA/AOyx0ul0ukkkTroB9cPUCDgXDnuQvE2VuP8AhsdFJ+Uyn/jYf9KA9x/wPv0eP8r/\\nAH65C9bs3LzH1huVqN1PwmIVqAT31hHYq987e430Ls/Ts+ToDpYuu3p2jOt1LKAMTk+ALk/0h72q\\n9y9TopmMregvZBGtsIDDPIzEbtMWJVu5y2u78qp8DfU7jMvc4hJDUuuxrXbws3s3hOxp5YFP111l\\nX6XHeV7gDoHQG+oPmnJaM+GwUq0sgBjselC9PYkaOK2SDIkZ1pvHcT9R2wA8a89D0fqLisPi8fRx\\nGKuXTDkEyFprkwSCdkX6YliT/lhtnZOz+g6gDXPMr0jV6zQ9M07KFWwxssTaYNscG4vabzFm83wm\\nTq3FyeDkEGH5SUrSYajABdaGNu5q/tJtBLEgJLEgnu7T5JHQByuJ2mnuRRietOkM9da9hREYiWji\\nBT8zSEDR7vrB7t9SGA57xzOX6qQYO3gsguQN9bDZeeSnAB9UnbGo7lLeR5IG28sB0ZcX4Uf9W28w\""
        var bitmapByteArray = Base64.decode(bitmapString, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.size)

        var testBitmap = ImageUtils.getCircularBitmap(bitmap)

        Assert.assertNotEquals(bitmap, testBitmap)
    }
}