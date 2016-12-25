import React from 'react';

import {
  View,
  Image,
  Text,
  TouchableOpacity,
  TouchableHighlight
} from 'react-native';

import {getToken} from '../util/Secret';
var PopupLoginRegPage = require('../../PopupLoginRegPage');

var Like = React.createClass({

  getInitialState: function(){
    return {
      counter: this.props.counter,
      isLiked: false,
      loginRegPageVisible: false,
    };

  },

  checkLogin:function(token) {
    console.log('like btn token:' + token);
    if(!token) {
      this.setState({loginRegPageVisible: true});
    } else {
      this.setState({
        counter : this.state.counter + 1,
      });
    }
  },

  pressLike: function() {
    getToken(this.checkLogin);
  },

  hideLoginRegPage: function() {
    this.setState({
      loginRegPageVisible: false,
    });
  },

  refresh: function(isLogin, token) {
    this.setState({
      loginRegPageVisible: false,
    }, this.props.refresh(isLogin, token));
  },

  render: function(){
    return (
      <View style={{flexDirection: 'row', }}>
        {this.state.loginRegPageVisible && <PopupLoginRegPage hideLoginRegPage={this.hideLoginRegPage} refresh={this.refresh}/>}
        <TouchableOpacity onPress={this.pressLike}>
          <Image style={{width:22, height:22, marginRight: 5}} source={require('../../imgs/like_empty.png')} />
        </TouchableOpacity>
        {/* <Text>{this.state.counter}</Text> */}
      </View>
    );
  }



});

module.exports = Like;
