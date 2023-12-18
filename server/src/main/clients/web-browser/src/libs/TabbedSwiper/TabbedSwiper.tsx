import 'swiper/css/bundle';

import {CSSProperties, ReactNode, useEffect, useState} from 'react';
import {
  Swiper,
  SwiperClass,
  SwiperProps,
  SwiperSlide,
  SwiperSlideProps,
} from 'swiper/react';
import {Tab, TabProps, Tabs, TabsProps} from '@mui/material';

export function TabbedSwiper<K>(props: {
  tabs: readonly {key: K; label: ReactNode; content: ReactNode}[];
  onTabChange?: (key: K) => void;
  containerStyle?: CSSProperties;
  swiperProps?: SwiperProps;
  swiperSlideProps?: SwiperSlideProps;
  tabsProps?: TabsProps;
  tabProps?: TabProps;
}) {
  const [activeTab, setActiveTab] = useState(0);
  const [tabSwiper, setTabSwiper] = useState<SwiperClass | undefined>();

  useEffect(() => {
    if (tabSwiper && !tabSwiper.destroyed) {
      tabSwiper.slideTo(activeTab);
    }
    if (props.onTabChange) {
      props.onTabChange(props.tabs[activeTab].key);
    }
  }, [activeTab]);

  return (
    <>
      <div
        style={{
          display: 'flex',
          flexFlow: 'column nowrap',
          height: '100%',
        }}
      >
        <Tabs
          value={activeTab}
          onChange={(e, newValue) => setActiveTab(newValue)}
          style={{flexShrink: 1}}
          {...props.tabsProps}
        >
          {props.tabs.map(tab => (
            <Tab key={String(tab.key)} label={tab.label} {...props.tabProps} />
          ))}
        </Tabs>
        <Swiper
          onSwiper={setTabSwiper}
          slidesPerView={1}
          loop={true}
          onSlideChange={swiper => setActiveTab(swiper.realIndex)}
          allowTouchMove={false}
          style={{
            alignSelf: 'stretch',
            flexGrow: 1,
            // This needs to stay here for tabs to work.
            margin: '0',
          }}
          {...props.swiperProps}
        >
          {props.tabs.map(tab => (
            <SwiperSlide key={String(tab.key)} style={{overflow: 'auto'}}>
              {tab.content}
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
    </>
  );
}
